package com.elboutique.backend.service;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elboutique.backend.DTO.request.AddressRequest;
import com.elboutique.backend.DTO.request.LoginRequest;
import com.elboutique.backend.DTO.request.RegisterRequest;
import com.elboutique.backend.DTO.response.AuthenticationResponse;
import com.elboutique.backend.config.JwtService;
import com.elboutique.backend.model.Customer;
import com.elboutique.backend.model.CustomerAddress;
import com.elboutique.backend.model.Phone;
import com.elboutique.backend.repository.CustomerAddressRepository;
import com.elboutique.backend.repository.CustomerRepository;
import com.elboutique.backend.repository.PhoneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = Customer.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

        customerRepository.save(user);

        var phone = Phone.builder()
            .phoneNumber(request.getPhoneNumber())
            .customer(user)
            .build();
        
        phoneRepository.save(phone);

        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            List<CustomerAddress> addresses = request.getAddresses().stream()
                .map((AddressRequest address) -> CustomerAddress.builder()
                    .governate(address.getGovernate())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .houseNumber(address.getHouseNumber())
                    .customer(user)
                    .build())
                .toList();
            customerAddressRepository.saveAll(addresses);
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken( request.getEmail(), request.getPassword())
        );
        var user = customerRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(jwtToken)
            .build();
    }

}
