package br.com.dillmann.fireflymobile.core.validation

import java.lang.RuntimeException

class ConsistencyException(val outcome: ValidationOutcome): RuntimeException()
