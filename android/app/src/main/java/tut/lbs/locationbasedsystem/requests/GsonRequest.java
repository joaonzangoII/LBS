package tut.lbs.locationbasedsystem.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/* Created by Aspsine on 2015/9/7. */
public class GsonRequest<T> extends Request<T> {
    private Response.Listener<T> mListener;
    private Class<T> mClazz;
    private Gson mGson;

    public GsonRequest(final String url,
                       final Class<T> clazz,
                       final Response.Listener<T> listener,
                       final Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    public GsonRequest(final int method,
                       final String url,
                       final Class<T> clazz,
                       final Response.Listener<T> listener,
                       final Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mClazz = clazz;
        this.mGson = new Gson();
    }

    public GsonRequest(final int method,
                       final String url,
                       final Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        T t = mGson.fromJson(parsed, mClazz);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(T t) {
        mListener.onResponse(t);
    }
}
