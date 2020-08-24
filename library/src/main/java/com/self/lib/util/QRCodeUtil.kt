package com.self.lib.util

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import com.google.zxing.BarcodeFormat.QR_CODE
import com.google.zxing.EncodeHintType
import com.google.zxing.EncodeHintType.CHARACTER_SET
import com.google.zxing.EncodeHintType.ERROR_CORRECTION
import com.google.zxing.EncodeHintType.MARGIN
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H
import java.util.Hashtable

object QRCodeUtil {

    /**
     * 生成二维码
     * @param content String  需要生成二维码的内容
     * @param width Int   二维码宽度
     * @param height Int  二维码高度
     * @return Bitmap?
     * @throws WriterException
     */
    @Throws(WriterException::class)
    @JvmStatic
    fun generateQRCode(content: String, width:Int, height:Int = width): Bitmap? {
        //生成
        val hints: Hashtable<EncodeHintType, Any?> = Hashtable()
        hints[CHARACTER_SET] = "UTF-8" //设置字符格式
        hints[ERROR_CORRECTION] = H //容错级别  敲Error+Le
        hints[MARGIN] = 0 //边框

        // 图像数据转换，使用了矩阵转换
        val bitMatrix = QRCodeWriter().encode(
            content,
            QR_CODE, width, height, hints
        )
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (bitMatrix[x, y]) {
                    pixels[y * width + x] = -0x1000000
                } else {
                    pixels[y * width + x] = -0x1
                }
            }
        }

        //设置为bitmap数据，以像素为单位
        val bitmap = Bitmap.createBitmap(width, height, ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

}