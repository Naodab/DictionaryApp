package com.midterm.testdictionary.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentCallBinding;
import com.midterm.testdictionary.repository.CallRepository;
import com.midterm.testdictionary.utils.DataModelType;

public class CallFragment extends Fragment implements CallRepository.Listener {
    FragmentCallBinding binding;
    CallRepository callRepository;
    boolean isCameraMuted = false;
    boolean isMicrophoneMuted = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        callRepository = CallRepository.getInstance();
        binding.callBtn.setOnClickListener(v->{
            //start a call request here
            callRepository.sendCallRequest(binding.targetUserNameEt.getText().toString(),()->{
                Toast.makeText(this.getContext(), "couldnt find the target", Toast.LENGTH_SHORT).show();
            });

        });
        callRepository.initLocalView(binding.localView);
        callRepository.initRemoteView(binding.remoteView);
        callRepository.listener = this;

        callRepository.subscribeForLatestEvent(data->{
            if (data.getType()== DataModelType.StartCall) {
                mHandler.post(()->{
                    binding.incomingNameTV.setText(data.getSender()+" is Calling you");
                    binding.incomingCallLayout.setVisibility(View.VISIBLE);
                    binding.acceptButton.setOnClickListener(v->{
                        //star the call here
                        callRepository.startCall(data.getSender());
                        binding.incomingCallLayout.setVisibility(View.GONE);
                    });
                    binding.rejectButton.setOnClickListener(v->{
                        binding.incomingCallLayout.setVisibility(View.GONE);
                    });
                });
            }
        });

        binding.switchCameraButton.setOnClickListener(v->{
            callRepository.switchCamera();
        });

        binding.micButton.setOnClickListener(v->{
            if (isMicrophoneMuted){
                binding.micButton.setImageResource(R.drawable.mic_off);
            }else {
                binding.micButton.setImageResource(R.drawable.mic);
            }
            callRepository.toggleAudio(isMicrophoneMuted);
            isMicrophoneMuted=!isMicrophoneMuted;
        });

        binding.videoButton.setOnClickListener(v->{
            if (isCameraMuted){
                binding.videoButton.setImageResource(R.drawable.cam_recorder_off);
            }else {
                binding.videoButton.setImageResource(R.drawable.cam_recorder);
            }
            callRepository.toggleVideo(isCameraMuted);
            isCameraMuted=!isCameraMuted;
        });

        binding.endCallButton.setOnClickListener(v->{
            callRepository.endCall();
//            finish();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCallBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void webrtcConnected() {

    }

    @Override
    public void webrtcClosed() {

    }
}