package br.com.felipeacerbi.popularmovies.utils;

public interface RequestCallback<T> {
    void onSuccess(T object);
    void onError(String error);
}
