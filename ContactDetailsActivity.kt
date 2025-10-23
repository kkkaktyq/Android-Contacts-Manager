package com.example.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactDetailsActivity : AppCompatActivity() {

    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnCall: Button
    private lateinit var btnBack: Button

    private var phoneNumber: String = ""

    companion object {
        private const val PERMISSIONS_REQUEST_CALL_PHONE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        ivPhoto = findViewById(R.id.ivContactPhotoDetail)
        tvName = findViewById(R.id.tvContactNameDetail)
        tvPhone = findViewById(R.id.tvContactPhoneDetail)
        btnCall = findViewById(R.id.btnCall)
        btnBack = findViewById(R.id.btnBack)

        val name = intent.getStringExtra("CONTACT_NAME") ?: "Sans nom"
        phoneNumber = intent.getStringExtra("CONTACT_PHONE") ?: ""
        val photoUri = intent.getStringExtra("CONTACT_PHOTO")

        tvName.text = name
        tvPhone.text = phoneNumber

        if (photoUri != null) {
            ivPhoto.setImageURI(Uri.parse(photoUri))
        } else {
            ivPhoto.setImageResource(R.drawable.ic_person_default)
        }

        btnCall.setOnClickListener {
            makePhoneCall()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun makePhoneCall() {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Numéro invalide", Toast.LENGTH_SHORT).show()
            return
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                PERMISSIONS_REQUEST_CALL_PHONE
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall()
                } else {
                    Toast.makeText(this, "Permission d'appel refusée", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
