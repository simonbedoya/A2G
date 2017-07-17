package com.monitoreosatelitalgps.a2g.Utils.Interface;

import com.monitoreosatelitalgps.a2g.Models.InfoPerson;

/**
 * Created by sbv23 on 15/07/2017.
 */

public interface ValidateTokenInterface {

    void errorWithDataUser();
    void successValidateToken(int cod, String username, String token);
}
