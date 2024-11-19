package com.g12.tpo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g12.tpo.server.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{}
