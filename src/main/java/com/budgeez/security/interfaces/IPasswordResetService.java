package com.budgeez.security.interfaces;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.security.entities.Token;

public interface IPasswordResetService {

    EGeneralResponse createPasswordResetToken(User user);

    EGeneralResponse resetPassword(String password, Token token);
}
