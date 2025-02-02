package com.pinup.service;

import com.pinup.dto.response.FriendRequestResponse;
import com.pinup.entity.Alarm;
import com.pinup.entity.FriendRequest;
import com.pinup.entity.Member;
import com.pinup.exception.*;
import com.pinup.global.exception.EntityAlreadyExistException;
import com.pinup.global.exception.EntityNotFoundException;
import com.pinup.global.exception.ErrorCode;
import com.pinup.global.util.AuthUtil;
import com.pinup.repository.AlarmRepository;
import com.pinup.repository.FriendRequestRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pinup.enums.FriendRequestStatus.PENDING;

@RequiredArgsConstructor
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final FriendShipService friendShipService;
    private final NotificationService notificationService;
    private final AlarmRepository alarmRepository;
    private final AuthUtil authUtil;

    @Transactional
    public FriendRequestResponse sendFriendRequest(Long receiverId) {

        String senderEmail = authUtil.getLoginMember().getEmail();

        Member sender = memberRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        validateSelfFriendRequest(sender, receiver);
        validateDuplicateFriendRequest(sender, receiver);
        validateAlreadyFriend(sender, receiver);

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
        friendRequestRepository.save(friendRequest);

        String message = sender.getName() + "님이 친구 요청을 보냈습니다.";
        notificationService.sendNotification(receiver.getEmail(),
                message);
        createAlarmFrom(receiver, message);

        return FriendRequestResponse.from(friendRequest);
    }

    private void validateSelfFriendRequest(Member sender, Member receiver) {
        if (sender.getEmail().equals(receiver.getEmail())) {
            throw new SelfFriendRequestException();
        }
    }

    private void validateDuplicateFriendRequest(Member sender, Member receiver) {
        friendRequestRepository.findBySenderAndReceiverAndFriendRequestStatus(sender, receiver, PENDING)
                .ifPresent(request -> {
                    throw new EntityAlreadyExistException(ErrorCode.ALREADY_EXIST_FRIEND_REQUEST);
                });
    }

    private void validateAlreadyFriend(Member sender, Member receiver) {
        if (friendShipService.existsFriendship(sender, receiver)) {
            throw new EntityAlreadyExistException(ErrorCode.ALREADY_FRIEND);
        }
    }

    @Transactional
    public FriendRequestResponse acceptFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        validateRequestReceiverIsCurrentUser(friendRequest);
        validateFriendRequestStatus(friendRequest);

        friendRequest.accept();
        friendRequestRepository.save(friendRequest);
        friendShipService.createFriendShip(friendRequest.getSender(), friendRequest.getReceiver());

        String message = friendRequest.getReceiver().getName() + "님이 친구 요청을 수락했습니다.";
        notificationService.sendNotification(friendRequest.getSender().getEmail(),
                message);
        createAlarmFrom(friendRequest.getReceiver(), message);

        return FriendRequestResponse.from(friendRequest);
    }

    @Transactional
    public FriendRequestResponse rejectFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        validateRequestReceiverIsCurrentUser(friendRequest);
        validateFriendRequestStatus(friendRequest);

        friendRequest.reject();
        friendRequestRepository.save(friendRequest);

        String message = friendRequest.getReceiver().getName() + "님이 친구 요청을 거절했습니다.";
        notificationService.sendNotification(friendRequest.getSender().getEmail(),
                message);
        createAlarmFrom(friendRequest.getReceiver(), message);

        return FriendRequestResponse.from(friendRequest);
    }

    private void validateFriendRequestStatus(FriendRequest friendRequest) {
        if (friendRequest.getFriendRequestStatus() != PENDING) {
            throw new EntityAlreadyExistException(ErrorCode.ALREADY_PROCESSED_FRIEND_REQUEST);
        }
    }

    private void validateRequestReceiverIsCurrentUser(FriendRequest friendRequest) {

        String currentUserEmail = authUtil.getLoginMember().getEmail();
        String friendRequestReceiverEmail = friendRequest.getReceiver().getEmail();

        if (!currentUserEmail.equals(friendRequestReceiverEmail)) {
            throw new FriendRequestReceiverMismatchException();
        }
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getReceivedFriendRequests() {

        String receiverEmail = authUtil.getLoginMember().getEmail();

        Member receiver = memberRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return friendRequestRepository.findByReceiver(receiver)
                .stream()
                .filter(request -> request.getFriendRequestStatus() == PENDING)
                .map(FriendRequestResponse::from)
                .collect(Collectors.toList());
    }

    private void createAlarmFrom(Member receiver, String message) {
        Alarm alarm = Alarm.builder()
                .message(message)
                .receiver(receiver)
                .build();
        alarmRepository.save(alarm);
    }

}
