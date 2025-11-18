package mefetran.dgusev.meddocs.app

import android.content.Context
import android.net.Uri
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun saveEncryptedPdf(context: Context, uri: Uri): File {
    val filesDir = File(context.filesDir, PDF_FILES_DIR)
    if (!filesDir.exists()) filesDir.mkdirs()

    val encryptedFile = File(filesDir, "${UUID.randomUUID()}.enc")

    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val encrypted = EncryptedFile.Builder(
        context,
        encryptedFile,
        masterKey,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    context.contentResolver.openInputStream(uri).use { inputStream ->
        encrypted.openFileOutput().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
    }

    return encryptedFile
}

fun decryptPdfToTempFile(context: Context, encryptedFile: File): File {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val encrypted = EncryptedFile.Builder(
        context,
        encryptedFile,
        masterKey,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    val tempPdf = File.createTempFile("dec_", ".pdf", context.cacheDir)

    encrypted.openFileInput().use { inputStream ->
        FileOutputStream(tempPdf).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }

    return tempPdf
}