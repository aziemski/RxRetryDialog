package com.example.api;

public class ApiResponse {

  public final String message;

  public ApiResponse(String message) {
    this.message = message;
  }

  @Override public String toString() {
    return "ApiResponse{" +
           "message='" + message + '\'' +
           '}';
  }
}
