package com.example.agriventure.interfaces

interface OnDataReceived {
    fun userFound(pin: String, key:String)
    fun userNotFound()
}