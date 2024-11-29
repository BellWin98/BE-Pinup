package com.pinup.repository;

import com.pinup.entity.Alarm;
import com.pinup.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findAllByReceiver(Member receiver, Pageable pageable);
}