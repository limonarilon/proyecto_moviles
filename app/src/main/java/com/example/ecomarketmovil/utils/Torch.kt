package com.example.ecomarketmovil.utils

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build

class Torch(context: Context) {

    private var cameraId: String? = null
    private val cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    init {
        try {
            // Se obtiene el ID de la cámara trasera, que es la que suele tener el flash.
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // Función para encender la linterna
    fun turnOn() {
        if (cameraId != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(cameraId!!, true)
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    // Función para apagar la linterna
    fun turnOff() {
        if (cameraId != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(cameraId!!, false)
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }
}