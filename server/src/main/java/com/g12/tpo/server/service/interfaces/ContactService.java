package com.g12.tpo.server.service.interfaces;

import java.util.List;

import com.g12.tpo.server.dto.ContactDTO;
import com.g12.tpo.server.entity.Contact;

public interface ContactService {

    Contact saveContact(ContactDTO contactDTO);

    Contact getContactById(Long id);

    List<Contact> getAllContacts();

    Contact updateContact(Long id, ContactDTO contactDTO);

    void deleteContact(Long id);

    List<Contact> getContactsByProblemType(String problemType);

    
}
