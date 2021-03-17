package com.birth.fit.exception.error

import java.lang.RuntimeException

class UserAlreadyExistException(msg: String?) : RuntimeException(msg)