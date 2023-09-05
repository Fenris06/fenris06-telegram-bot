package ru.fenris06.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fenris06.dao.AppUserRepository;
import ru.fenris06.service.UserActivationService;
import ru.fenris06.utils.CryptoTool;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;


    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optional = appUserRepository.findById(userId);
        if (optional.isPresent()) {
            var user = optional.get();
            user.setIsActive(true);
            appUserRepository.save(user);
            return true;
        }
        return false;
    }
}
