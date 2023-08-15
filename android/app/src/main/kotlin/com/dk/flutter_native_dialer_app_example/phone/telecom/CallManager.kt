package com.dk.flutter_native_dialer_app_example.phone.telecom

import android.annotation.SuppressLint
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.telecom.VideoProfile
import java.util.concurrent.CopyOnWriteArraySet

class CallManager {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var inCallService: InCallService? = null
        private var call: Call? = null
        private val calls = mutableListOf<Call>()
        private val listeners = CopyOnWriteArraySet<CallManagerListener>()

        //todo: add if have call
        fun onCallAdded(call: Call) {
            this.call = call
            calls.add(call)
            for (listener in listeners) {
                listener.onPrimaryCallChanged(call)
            }
            call.registerCallback(object : Call.Callback() {
                override fun onStateChanged(call: Call, state: Int) {
                    updateState()
                }

                override fun onDetailsChanged(call: Call, details: Call.Details) {
                    updateState()
                }

                override fun onConferenceableCallsChanged(call: Call, conferenceableCalls: MutableList<Call>) {
                    updateState()
                }
            })
        }

        //todo: removed if end call
        fun onCallRemoved(call: Call) {
            calls.remove(call)
            updateState()
        }

        //todo: get call
        fun getCalls(): MutableList<Call> {
            return calls
        }

        //todo: get data and information in call
        fun getPhoneState(): PhoneState {
            return when (calls.size) {
                0 -> NoCall
                1 -> SingleCall(calls.first())
                2 -> {
                    val active = calls.find { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        it.details.state == Call.STATE_ACTIVE
                    } else {
                        it.state == Call.STATE_ACTIVE
                    }
                    }
                    val newCall = calls.find { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        it.details.state == Call.STATE_CONNECTING || it.details.state == Call.STATE_DIALING
                    } else {
                        it.state == Call.STATE_CONNECTING || it.state == Call.STATE_DIALING
                    }
                    }
                    val onHold = calls.find { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        it.details.state == Call.STATE_HOLDING
                    } else {
                        it.state == Call.STATE_HOLDING
                    }
                    }
                    if (active != null && newCall != null) {
                        TwoCalls(newCall, active)
                    } else if (newCall != null && onHold != null) {
                        TwoCalls(newCall, onHold)
                    } else if (active != null && onHold != null) {
                        TwoCalls(active, onHold)
                    } else {
                        TwoCalls(calls[0], calls[1])
                    }
                }
                else -> {
                    val conference = calls.find { it.details.hasProperty(Call.Details.PROPERTY_CONFERENCE) } ?: return NoCall
                    val secondCall = if (conference.children.size + 1 != calls.size) {
                        calls.filter { !it.details.hasProperty(Call.Details.PROPERTY_CONFERENCE) }
                            .subtract(conference.children.toSet())
                            .firstOrNull()
                    } else {
                        null
                    }
                    if (secondCall == null) {
                        SingleCall(conference)
                    } else {
                        val newCallState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            secondCall.details.state
                        } else {
                            secondCall.state
                        }
                        if (newCallState == Call.STATE_ACTIVE || newCallState == Call.STATE_CONNECTING || newCallState == Call.STATE_DIALING) {
                            TwoCalls(secondCall, conference)
                        } else {
                            TwoCalls(conference, secondCall)
                        }
                    }
                }
            }
        }

        // todo: call update state
        private fun updateState() {
            val primaryCall = when (val phoneState = getPhoneState()) {
                is NoCall -> null
                is SingleCall -> phoneState.call
                is TwoCalls -> phoneState.active
            }
            var notify = true
            if (primaryCall == null) {
                call = null
            } else if (primaryCall != call) {
                call = primaryCall
                for (listener in listeners) {
                    listener.onPrimaryCallChanged(primaryCall)
                }
                notify = false
            }
            if (notify) {
                for (listener in listeners) {
                    listener.onStateChanged()
                }
            }
        }

        //todo: get primary call
        fun getPrimaryCall(): Call? {
            return call
        }

        //todo: get list conference calls
        fun getConferenceCalls(): List<Call> {
            return calls.find { it.details.hasProperty(Call.Details.PROPERTY_CONFERENCE) }?.children ?: emptyList()
        }

        //todo: accept call
        fun accept() {
            call?.answer(VideoProfile.STATE_AUDIO_ONLY)
        }

        //todo: reject call
        fun reject() {
            if (call != null) {
                if (getState() == Call.STATE_RINGING) {
                    call!!.reject(false, null)
                } else {
                    call!!.disconnect()
                }
            }
        }

        //todo: toggleHold
        fun toggleHold(): Boolean {
            val isOnHold = getState() == Call.STATE_HOLDING
            if (isOnHold) {
                call?.unhold()
            } else {
                call?.hold()
            }
            return !isOnHold
        }

        //todo: merge and swap call - hoán đổi và hợp nhất 2 cuộc gọi với nhau
        fun swap() {
            if (calls.size > 1) {
                calls.find { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    it.details.state == Call.STATE_HOLDING
                } else {
                    it.state == Call.STATE_HOLDING
                }
                }?.unhold()
            }
        }

        fun merge() {
            val conferenceableCalls = call!!.conferenceableCalls
            if (conferenceableCalls.isNotEmpty()) {
                call!!.conference(conferenceableCalls.first())
            } else {
                if ((call!!.details.callCapabilities and Call.Details.CAPABILITY_MERGE_CONFERENCE) != 0) {
                    call!!.mergeConference()
                }
            }
        }

        //todo: add and remove call status: - thêm và xoá lắng nghe sự kiện trạng thái cuộc gọi
        fun addListener(listener: CallManagerListener) {
            listeners.add(listener)
        }

        fun removeListener(listener: CallManagerListener) {
            listeners.remove(listener)
        }

        //todo: getState() primary call - trả về trạng thái của cuộc gọi chính
        fun getState() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getPrimaryCall()?.details?.state
        } else {
            getPrimaryCall()?.state
        }

        //todo: send DTMF ( nhập pad trong cuộc gọi)
        fun keypad(c: Char) {
            call?.playDtmfTone(c)
            call?.stopDtmfTone()
        }
    }
}

interface CallManagerListener {
    fun onStateChanged()
    fun onPrimaryCallChanged(call: Call)
}

sealed class PhoneState
object NoCall : PhoneState()
class SingleCall(val call: Call) : PhoneState()
class TwoCalls(val active: Call, val onHold: Call) : PhoneState()