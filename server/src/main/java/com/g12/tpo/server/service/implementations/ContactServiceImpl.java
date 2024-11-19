package com.g12.tpo.server.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.dto.ContactDTO;
import com.g12.tpo.server.entity.Contact;
import com.g12.tpo.server.repository.ContactRepository;
import com.g12.tpo.server.service.interfaces.ContactService;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService{
    
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact saveContact(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setFullName(contactDTO.getFullName());
        contact.setProblemType(contactDTO.getProblemType());
        contact.setDescription(contactDTO.getDescription());
        contact.setPhotoUrls(contactDTO.getPhotoUrls());
        return contactRepository.save(contact);
    }

    @Override 
    public Contact getContactById(Long id) { 
        return contactRepository.findById(id) .orElseThrow(() -> new RuntimeException("Contacto no encontrado con ID: " + id)); 
    } 
    
    @Override 
    public List<Contact> getAllContacts() { 
        return contactRepository.findAll(); 
    } 
    
    @Override 
    public Contact updateContact(Long id, ContactDTO contactDTO) { 
        Contact contact = getContactById(id); 
        Optional.ofNullable(contactDTO.getFullName()).ifPresent(contact::setFullName); 
        Optional.ofNullable(contactDTO.getProblemType()).ifPresent(contact::setProblemType); 
        Optional.ofNullable(contactDTO.getDescription()).ifPresent(contact::setDescription); 
        Optional.ofNullable(contactDTO.getPhotoUrls()).ifPresent(contact::setPhotoUrls); 
        return contactRepository.save(contact); 
    } 
    
    @Override
    public void deleteContact(Long id) { 
        if (!contactRepository.existsById(id)) { 
            throw new RuntimeException("Contacto no encontrado con ID: " + id); 
        } contactRepository.deleteById(id); 
    } 
    
    @Override 
    public List<Contact> getContactsByProblemType(String problemType) { 
        return contactRepository.findAll().stream() .filter(contact -> problemType.equalsIgnoreCase(contact.getProblemType())) .toList(); 
    }
}
