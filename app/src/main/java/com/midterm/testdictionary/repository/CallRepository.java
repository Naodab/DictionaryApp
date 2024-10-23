package com.midterm.testdictionary.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.midterm.testdictionary.remote.CallClient;
import com.midterm.testdictionary.utils.DataModel;
import com.midterm.testdictionary.utils.DataModelType;
import com.midterm.testdictionary.utils.ErrorCallBack;
import com.midterm.testdictionary.utils.NewEventCallback;
import com.midterm.testdictionary.utils.SuccessCallBack;
import com.midterm.testdictionary.utils.FailureCallBack;
import com.midterm.testdictionary.webrtc.MyPeerConnectionObserver;
import com.midterm.testdictionary.webrtc.WebRTCClient;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;

public class CallRepository  implements WebRTCClient.Listener {
    public Listener listener;
    private final Gson gson = new Gson();
    private final CallClient callClient;

    private WebRTCClient webRTCClient;

    private String currentUsername;

    private SurfaceViewRenderer remoteView;

    private String target;
    private void updateCurrentUsername(String username){
        this.currentUsername = username;
    }

    private CallRepository(){
        this.callClient = new CallClient();
    }

    private static CallRepository instance;
    public static CallRepository getInstance(){
        if (instance == null){
            instance = new CallRepository();
        }
        return instance;
    }

    public void login(String username, Context context, SuccessCallBack callBack, FailureCallBack failureCallBack) {
        if (username == null || username.isEmpty()) {
            // Kiểm tra username rỗng
            if (failureCallBack != null) {
                failureCallBack.onFailure("Username không được để trống");
            }
            return;
        }

        try {
            callClient.login(username, () -> {
                updateCurrentUsername(username);
                this.webRTCClient = new WebRTCClient(context, new MyPeerConnectionObserver() {
                    @Override
                    public void onAddStream(MediaStream mediaStream) {
                        super.onAddStream(mediaStream);
                        try {
                            mediaStream.videoTracks.get(0).addSink(remoteView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                        Log.d("TAG", "onConnectionChange: " + newState);
                        super.onConnectionChange(newState);
                        if (newState == PeerConnection.PeerConnectionState.CONNECTED && listener != null) {
                            listener.webrtcConnected();
                        }

                        if (newState == PeerConnection.PeerConnectionState.CLOSED ||
                                newState == PeerConnection.PeerConnectionState.DISCONNECTED) {
                            if (listener != null) {
                                listener.webrtcClosed();
                            }
                        }
                    }

                    @Override
                    public void onIceCandidate(IceCandidate iceCandidate) {
                        super.onIceCandidate(iceCandidate);
                        webRTCClient.sendIceCandidate(iceCandidate, target);
                    }
                }, username);
                webRTCClient.listener = this;
                callBack.onSuccess();
            });
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình gọi login
            if (failureCallBack != null) {
                failureCallBack.onFailure("Đăng nhập thất bại: " + e.getMessage());
            }
        }
    }



    public void initLocalView(SurfaceViewRenderer view){
        webRTCClient.initLocalSurfaceView(view);
    }

    public void initRemoteView(SurfaceViewRenderer view){
        webRTCClient.initRemoteSurfaceView(view);
        this.remoteView = view;
    }

    public void startCall(String target){
        webRTCClient.call(target);
    }

    public void switchCamera() {
        webRTCClient.switchCamera();
    }

    public void toggleAudio(Boolean shouldBeMuted){
        webRTCClient.toggleAudio(shouldBeMuted);
    }
    public void toggleVideo(Boolean shouldBeMuted){
        webRTCClient.toggleVideo(shouldBeMuted);
    }
    public void sendCallRequest(String target, ErrorCallBack errorCallBack){
        callClient.sendMessageToOtherUser(
                new DataModel(target,currentUsername,null, DataModelType.StartCall),errorCallBack
        );
    }

    public void endCall(){
        webRTCClient.closeConnection();
    }

    public void subscribeForLatestEvent(NewEventCallback callBack){
        callClient.observeIncomingLatestEvent(model -> {
            switch (model.getType()){

                case Offer:
                    this.target = model.getSender();
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.OFFER,model.getData()
                    ));
                    webRTCClient.answer(model.getSender());
                    break;
                case Answer:
                    this.target = model.getSender();
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.ANSWER,model.getData()
                    ));
                    break;
                case IceCandidate:
                    try{
                        IceCandidate candidate = gson.fromJson(model.getData(),IceCandidate.class);
                        webRTCClient.addIceCandidate(candidate);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case StartCall:
                    this.target = model.getSender();
                    callBack.onNewEventReceived(model);
                    break;
            }

        });
    }

    @Override
    public void onTransferDataToOtherPeer(DataModel model) {
        callClient.sendMessageToOtherUser(model,()->{});
    }

    public interface Listener{
        void webrtcConnected();
        void webrtcClosed();
    }
}