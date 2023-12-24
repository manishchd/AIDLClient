package com.manish.aidlclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import androidx.appcompat.app.AppCompatActivity
import com.manish.aidlclient.databinding.ActivityMainBinding
import com.manish.aidlserver.IColorAidlInterface

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var iColorAidlInterface: IColorAidlInterface? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            iColorAidlInterface = IColorAidlInterface.Stub.asInterface(binder)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            iColorAidlInterface = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkAidlColor()
        clickHandler()
    }

    private fun clickHandler() {
        binding.aidlServiceCall.setOnClickListener {
            try {
                binding.aidlServiceCall.setBackgroundColor(iColorAidlInterface?.color ?: Color.RED)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    private fun checkAidlColor() {
        val intent = Intent("AIDLColorService")
        intent.setPackage("com.manish.aidlserver")
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }


}