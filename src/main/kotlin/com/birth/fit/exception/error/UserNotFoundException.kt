package com.birth.fit.exception.error

import java.lang.RuntimeException

class UserNotFoundException(msg: String?) : RuntimeException(msg)