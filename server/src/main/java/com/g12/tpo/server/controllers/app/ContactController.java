package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.dto.ContactDTO;
import com.g12.tpo.server.entity.Contact;
import com.g12.tpo.server.service.interfaces.ContactService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<String> createContact( @RequestBody ContactDTO contactDTO) {
        contactService.saveContact(contactDTO);
        return ResponseEntity.ok("El problema ha sido registrado exitosamente.");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        Contact contact = contactService.getContactById(id);
        ContactDTO contactDTO = ContactDTO.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .problemType(contact.getProblemType())
                .description(contact.getDescription())
                .photoUrls(contact.getPhotoUrls())
                .build();
        return ResponseEntity.ok(contactDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.updateContact(id, contactDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok("El contacto ha sido eliminado exitosamente.");
    }

    @GetMapping("/problem/{type}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Contact>> getContactsByProblemType(@PathVariable String type) {
        return ResponseEntity.ok(contactService.getContactsByProblemType(type));
    }
}


