package com.pinup.repository;

import com.pinup.entity.Chat;
import com.pinup.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByChatRoom(ChatRoom chatRoom);
}
