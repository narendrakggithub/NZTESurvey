package com.nkg.pmbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nkg.pmbot.model.HexStrings;
import com.nkg.pmbot.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface HexStringRepository extends JpaRepository<HexStrings, Long> {
	Optional<HexStrings> findByValue(String value);

	Optional<User> findByValueAndCategory(String value, String category);

	List<User> findByIdIn(List<Long> hexStringsIds);

	Boolean existsByValue(String value);

}