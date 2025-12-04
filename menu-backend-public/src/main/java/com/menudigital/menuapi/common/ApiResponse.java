package com.menudigital.menuapi.common;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ApiResponse<T> {
  private boolean success; private String message; private T data;
  public static <T> ApiResponse<T> ok(T d){ return ApiResponse.<T>builder().success(true).message("OK").data(d).build(); }
  public static <T> ApiResponse<T> msg(String m){ return ApiResponse.<T>builder().success(true).message(m).build(); }
}
