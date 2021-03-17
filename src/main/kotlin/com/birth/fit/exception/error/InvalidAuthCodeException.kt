package com.birth.fit.exception.error

import java.lang.RuntimeException

class InvalidAuthCodeException(msg: String?): RuntimeException(msg)