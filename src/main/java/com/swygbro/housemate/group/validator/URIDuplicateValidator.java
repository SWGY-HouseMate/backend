package com.swygbro.housemate.group.validator;

import com.swygbro.housemate.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class URIDuplicateValidator implements ValidatorURI {

    private final GroupRepository groupRepository;

    @Override
    public Boolean valid(String uri) {
        return groupRepository.findByLink(uri).isPresent();
    }
}