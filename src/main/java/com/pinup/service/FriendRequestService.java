package com.pinup.service;

import com.pinup.dto.response.FriendRequestResponse;
import com.pinup.entity.Alarm;
import com.pinup.entity.FriendRequest;
import com.pinup.entity.Member;
import com.pinup.exception.*;
import com.pinup.repository.AlarmRepository;
import com.pinup.repository.FriendRequestRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Transactional
    public FriendRequestResponse sendFriendRequest(Long receiverId) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Member sender = memberRepository.findByEmail(senderEmail)
                .orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(MemberNotFoundException::new);

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
                    throw new AlreadyExistFriendRequestException();
                });
    }

    private void validateAlreadyFriend(Member sender, Member receiver) {
        if (friendShipService.existsFriendship(sender, receiver)) {
            throw new AlreadyFriendException();
        }
    }

    @Transactional
    public FriendRequestResponse acceptFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(FriendRequestNotFoundException::new);
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
                .orElseThrow(FriendRequestNotFoundException::new);
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
            throw new AlreadyProcessedFriendRequestException();
        }
    }

    private void validateRequestReceiverIsCurrentUser(FriendRequest friendRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String friendRequestReceiverEmail = friendRequest.getReceiver().getEmail();

        if (!currentUserEmail.equals(friendRequestReceiverEmail)) {
            throw new FriendRequestReceiverMismatchException();
        }
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getReceivedFriendRequests() {
        String receiverEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Member receiver = memberRepository.findByEmail(receiverEmail)
                .orElseThrow(MemberNotFoundException::new);

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
