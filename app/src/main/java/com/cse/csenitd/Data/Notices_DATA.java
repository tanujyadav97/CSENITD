package com.cse.csenitd.Data;

/**
 * Created by lenovo on 23-06-2017.Mohit yaadav
 */

public class Notices_DATA {
    String _name;
    String _post;
    String _dtm;

    public Notices_DATA(String _name, String _post, String _dtm) {
        this._name = _name;
        this._post = _post;

        this._dtm = _dtm;
    }

    public String get_dtm() {
        return _dtm;
    }

    public void set_dtm(String _dtm) {
        this._dtm = _dtm;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_post() {
        return _post;
    }

    public void set_post(String _post) {
        this._post = _post;
    }
}
