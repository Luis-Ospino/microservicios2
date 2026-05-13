package com.example.mscuentasmovimientos.exception;

public class CuentaDuplicadaException extends RuntimeException {
    public CuentaDuplicadaException(String numeroCuenta) {
        super("Ya existe una cuenta con el número: " + numeroCuenta);
    }
}
