package com.mymusicplayer.helper.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Administrator on 2015/6/8 0008.
 */
public class ImageUtil {

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
// ���ͼƬ�Ŀ��
        int width = bm.getWidth();
        int height = bm.getHeight();
        // �������ű���
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // ȡ����Ҫ���ŵ�matrix����
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // �õ��µ�ͼƬ
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
