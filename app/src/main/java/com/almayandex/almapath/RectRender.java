package com.almayandex.almapath;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.almayandex.domain.Travel;

import ru.yandex.yandexmapkit.overlay.IRender;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

/**
 * Created by Александр on 03.01.2017.
 */

public class RectRender implements IRender {
    private Travel travel;

    public RectRender(Travel t){
        this.travel = t;
    }
    @Override
    public void draw(Canvas canvas, OverlayItem overlayItem) {
        // TODO Auto-generated method stub

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//какой то флаг
        mPaint.setStyle(Paint.Style.STROKE);//тип линии
        mPaint.setStrokeWidth(6);//размер линии в пикселях
        mPaint.setColor(travel.getColor());
        OverlayRectItem item = (OverlayRectItem) overlayItem;
        Path p = new Path();
        if (item.screenPoint != null && item.screenPoint.size() > 0) {
            ScreenPoint screenPoint = item.screenPoint.get(0);
            p.moveTo(screenPoint.getX(), screenPoint.getY());

            for (int i = 1; i < item.screenPoint.size(); i++) {
                screenPoint = item.screenPoint.get(i);
                p.lineTo(screenPoint.getX(), screenPoint.getY());
            }
            canvas.drawPath(p, mPaint);
        }
    }
}
