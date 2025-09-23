package com.uade.tpo.marketplace.exceptions;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.exceptions.Auth.ForbiddenException;

public class SecurityUtils {
    public static void checkOwnerOrThrow(User currentUser, User resourceOwner) {
        if (currentUser == null || resourceOwner == null || !currentUser.getId().equals(resourceOwner.getId())) {
            throw new ForbiddenException("No tenés permisos para hacer esto");
        }
    }
}
