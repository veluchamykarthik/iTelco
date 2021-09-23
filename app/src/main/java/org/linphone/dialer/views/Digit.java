/*
 * Copyright (c) 2010-2019 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.dialer.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import org.linphone.LinphoneContext;
import org.linphone.LinphoneManager;
import org.linphone.R;
import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.tools.Log;
import org.linphone.settings.LinphonePreferences;

@SuppressLint("AppCompatCustomView")
public class Digit extends Button implements AddressAware {
    private boolean mPlayDtmf;
    private AddressText mAddress;
    int number2Flag = 0;
    String number2Var = "";
    Boolean number2TimerOver = true;
    int number3Flag = 0;
    String number3Var = "";
    Boolean number3TimerOver = true;
    int number4Flag = 0;
    String number4Var = "";
    Boolean number4TimerOver = true;
    int number5Flag = 0;
    String number5Var = "";
    Boolean number5TimerOver = true;
    int number6Flag = 0;
    String number6Var = "";
    Boolean number6TimerOver = true;
    int number7Flag = 0;
    String number7Var = "";
    Boolean number7TimerOver = true;
    int number8Flag = 0;
    String number8Var = "";
    Boolean number8TimerOver = true;
    int number9Flag = 0;
    String number9Var = "";
    Boolean number9TimerOver = true;

    public Digit(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context, attrs);
    }

    public Digit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Digit(Context context) {
        super(context);
        setLongClickable(true);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Numpad);
        mPlayDtmf = 1 == a.getInt(R.styleable.Numpad_play_dtmf, 1);
        a.recycle();

        setLongClickable(true);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);

        if (text == null || text.length() < 1) {
            return;
        }

        DialKeyListener lListener = new DialKeyListener();
        setOnClickListener(lListener);
        setOnTouchListener(lListener);

        if ("0+".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("1".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        /*        if ("2".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("3".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("4".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("5".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("6".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("7".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("8".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("9".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }*/
    }

    @Override
    public void setAddressWidget(AddressText address) {
        mAddress = address;
    }

    private class DialKeyListener implements OnClickListener, OnTouchListener, OnLongClickListener {
        final char mKeyCode;
        boolean mIsDtmfStarted;

        DialKeyListener() {
            mKeyCode = Digit.this.getText().subSequence(0, 1).charAt(0);
        }

        private boolean linphoneServiceReady() {
            if (!LinphoneContext.isReady()) {
                Log.e("[Numpad] Service is not ready while pressing digit");
                return false;
            }
            return true;
        }

        public void onClick(View v) {
            int id = v.getId();
            if (mPlayDtmf) {
                if (!linphoneServiceReady()) return;
                Core core = LinphoneManager.getCore();
                core.stopDtmf();
                mIsDtmfStarted = false;
                Call call = core.getCurrentCall();
                if (call != null) {
                    call.sendDtmf(mKeyCode);
                }
            }

            if (mAddress != null) {
                int begin = mAddress.getSelectionStart();
                if (begin == -1) {
                    begin = mAddress.length();
                }
                /*if (begin >= 0) {
                    if (R.id.Digit2 == id) {
                        if (number2Flag == 0) {
                            if (number2TimerOver) {
                                number2TimerOver = false;
                                startTimer(begin);
                            }
                        }
                        if (number2Flag == 0) {
                            number2Flag += 1;
                            number2Var = "a";
                        } else if (number2Flag == 1) {
                            number2Flag += 1;
                            number2Var = "b";
                        } else if (number2Flag == 2) {
                            number2Flag = 0;
                            number2Var = "c";
                        }
                    }
                    if (R.id.Digit3 == id) {
                        if (number3Flag == 0) {
                            if (number3TimerOver) {
                                number3TimerOver = false;
                                startTimer3(begin);
                            }
                        }
                        if (number3Flag == 0) {
                            number3Flag += 1;
                            number3Var = "d";
                        } else if (number3Flag == 1) {
                            number3Flag += 1;
                            number3Var = "e";
                        } else if (number3Flag == 2) {
                            number3Flag = 0;
                            number3Var = "f";
                        }
                    }
                    if (R.id.Digit4 == id) {
                        if (number4Flag == 0) {
                            if (number4TimerOver) {
                                number4TimerOver = false;
                                startTimer4(begin);
                            }
                        }
                        if (number4Flag == 0) {
                            number4Flag += 1;
                            number4Var = "g";
                        } else if (number4Flag == 1) {
                            number4Flag += 1;
                            number4Var = "h";
                        } else if (number4Flag == 2) {
                            number4Flag = 0;
                            number4Var = "i";
                        }
                    }
                    if (R.id.Digit5 == id) {
                        if (number5Flag == 0) {
                            if (number5TimerOver) {
                                number5TimerOver = false;
                                startTimer5(begin);
                            }
                        }
                        if (number5Flag == 0) {
                            number5Flag += 1;
                            number5Var = "j";
                        } else if (number5Flag == 1) {
                            number5Flag += 1;
                            number5Var = "k";
                        } else if (number5Flag == 2) {
                            number5Flag = 0;
                            number5Var = "l";
                        }
                    }
                    if (R.id.Digit6 == id) {
                        if (number6Flag == 0) {
                            if (number6TimerOver) {
                                number6TimerOver = false;
                                startTimer6(begin);
                            }
                        }
                        if (number6Flag == 0) {
                            number6Flag += 1;
                            number6Var = "m";
                        } else if (number6Flag == 1) {
                            number6Flag += 1;
                            number6Var = "n";
                        } else if (number6Flag == 2) {
                            number6Flag = 0;
                            number6Var = "o";
                        }
                    }
                    if (R.id.Digit7 == id) {
                        if (number7Flag == 0) {
                            if (number7TimerOver) {
                                number7TimerOver = false;
                                startTimer7(begin);
                            }
                        }
                        if (number7Flag == 0) {
                            number7Flag += 1;
                            number7Var = "p";
                        } else if (number7Flag == 1) {
                            number7Flag += 1;
                            number7Var = "q";
                        } else if (number7Flag == 2) {
                            number7Flag += 1;
                            number7Var = "r";
                        } else if (number7Flag == 3) {
                            number7Flag = 0;
                            number7Var = "s";
                        }
                    }
                    if (R.id.Digit8 == id) {
                        if (number8Flag == 0) {
                            if (number8TimerOver) {
                                number8TimerOver = false;
                                startTimer8(begin);
                            }
                        }
                        if (number8Flag == 0) {
                            number8Flag += 1;
                            number8Var = "t";
                        } else if (number8Flag == 1) {
                            number8Flag += 1;
                            number8Var = "u";
                        } else if (number8Flag == 2) {
                            number8Flag = 0;
                            number8Var = "v";
                        }
                    }
                    if (R.id.Digit9 == id) {
                        if (number9Flag == 0) {
                            if (number9TimerOver) {
                                number9TimerOver = false;
                                startTimer9(begin);
                            }
                        }
                        if (number9Flag == 0) {
                            number9Flag += 1;
                            number9Var = "w";
                        } else if (number9Flag == 1) {
                            number9Flag += 1;
                            number9Var = "x";
                        } else if (number9Flag == 2) {
                            number9Flag += 1;
                            number9Var = "y";
                        } else if (number9Flag == 3) {
                            number9Flag = 0;
                            number9Var = "z";
                        }
                    }
                }*/
                if (begin >= 0) {
                    mAddress.getEditableText().insert(begin, String.valueOf(mKeyCode));
                }
                if (LinphonePreferences.instance().getDebugPopupAddress() != null
                        && mAddress.getText()
                                .toString()
                                .equals(LinphonePreferences.instance().getDebugPopupAddress())) {
                    displayDebugPopup();
                }
            }
        }

        void startTimer(final int begin) {
            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number2Flag = 0;
                            mAddress.getEditableText().insert(begin, number2Var);
                            number2TimerOver = true;
                            //                            number3Flag = 0;
                            //                            mAddress.getEditableText().insert(begin,
                            // number3Var);
                            //                            number3TimerOver = true;
                        }
                    },
                    500);
        }

        void startTimer3(final int begin) {
            Handler handler1 = new Handler();
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number3Flag = 0;
                            mAddress.getEditableText().insert(begin, number3Var);
                            number3TimerOver = true;
                        }
                    },
                    500);
        }

        void startTimer4(final int begin) {
            Handler handler1 = new Handler();
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number4Flag = 0;
                            mAddress.getEditableText().insert(begin, number4Var);
                            number4TimerOver = true;
                        }
                    },
                    500);
        }

        void startTimer5(final int begin) {
            Handler handler1 = new Handler();
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number5Flag = 0;
                            mAddress.getEditableText().insert(begin, number5Var);
                            number5TimerOver = true;
                        }
                    },
                    500);
        }

        void startTimer6(final int begin) {
            Handler handler1 = new Handler();
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number6Flag = 0;
                            mAddress.getEditableText().insert(begin, number6Var);
                            number6TimerOver = true;
                        }
                    },
                    500);
        }

        void startTimer7(final int begin) {
            Handler handler1 = new Handler();
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number7Flag = 0;
                            mAddress.getEditableText().insert(begin, number7Var);
                            number7TimerOver = true;
                        }
                    },
                    700);
        }

        void startTimer8(final int begin) {
            Handler handler1 = new Handler();
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number8Flag = 0;
                            mAddress.getEditableText().insert(begin, number8Var);
                            number8TimerOver = true;
                        }
                    },
                    500);
        }

        void startTimer9(final int begin) {
            Handler handler1 = new Handler();
            handler1.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            // run something after 5 milli sec delay
                            number9Flag = 0;
                            mAddress.getEditableText().insert(begin, number9Var);
                            number9TimerOver = true;
                        }
                    },
                    700);
        }

        void displayDebugPopup() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(getContext().getString(R.string.debug_popup_title));
            if (LinphonePreferences.instance().isDebugEnabled()) {
                alertDialog.setItems(
                        getContext().getResources().getStringArray(R.array.popup_send_log),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    LinphonePreferences.instance().setDebugEnabled(false);
                                }
                                if (which == 1) {
                                    Core core = LinphoneManager.getCore();
                                    if (core != null) {
                                        core.uploadLogCollection();
                                    }
                                }
                            }
                        });

            } else {
                alertDialog.setItems(
                        getContext().getResources().getStringArray(R.array.popup_enable_log),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    LinphonePreferences.instance().setDebugEnabled(true);
                                }
                            }
                        });
            }
            alertDialog.show();
            mAddress.getEditableText().clear();
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (!mPlayDtmf) return false;
            if (!linphoneServiceReady()) return true;

            LinphoneManager.getCallManager().resetCallControlsHidingTimer();

            Core core = LinphoneManager.getCore();
            if (event.getAction() == MotionEvent.ACTION_DOWN && !mIsDtmfStarted) {
                LinphoneManager.getCallManager()
                        .playDtmf(getContext().getContentResolver(), mKeyCode);
                mIsDtmfStarted = true;
            } else {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    core.stopDtmf();
                    mIsDtmfStarted = false;
                }
            }
            return false;
        }

        public boolean onLongClick(View v) {
            int id = v.getId();
            Core core = LinphoneManager.getCore();

            if (mPlayDtmf) {
                if (!linphoneServiceReady()) return true;
                // Called if "0+" dtmf
                core.stopDtmf();
            }

            if (id == R.id.Digit1 && core.getCalls().length == 0) {
                String voiceMail = LinphonePreferences.instance().getVoiceMailUri();
                mAddress.getEditableText().clear();
                if (voiceMail != null) {
                    mAddress.getEditableText().append(voiceMail);
                    LinphoneManager.getCallManager().newOutgoingCall(mAddress);
                }
                return true;
            }
            if (id == R.id.Digit2 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(2));
                return true;
            }
            if (id == R.id.Digit3 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(3));
                return true;
            }
            if (id == R.id.Digit4 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(4));
                return true;
            }
            if (id == R.id.Digit5 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(5));
                return true;
            }
            if (id == R.id.Digit6 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(6));
                return true;
            }
            if (id == R.id.Digit7 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(7));
                return true;
            }
            if (id == R.id.Digit8 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(8));
                return true;
            }
            if (id == R.id.Digit9 /* && core.getCalls().length == 0*/) {
                mAddress.getEditableText().insert(mAddress.length(), String.valueOf(9));
                return true;
            }

            if (mAddress == null) return true;

            int begin = mAddress.getSelectionStart();
            if (begin == -1) {
                begin = mAddress.getEditableText().length();
            }
            if (begin >= 0) {
                mAddress.getEditableText().insert(begin, "+");
            }
            return true;
        }
    }
}
