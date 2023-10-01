package com.bs.application.utils;

import com.bs.application.dtos.Response;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bs.application.utils.ResponseStatus.FAIL;
import static com.bs.application.utils.ResponseStatus.OK;

@Service
public class ResponseUtil {

    public Response generateSuccessResponse(Object obj) {
        return Response.builder()
                .status(OK.name())
                .data(List.of(obj)).build();
    }

    public Response generateFailureResponse(String error){
        return Response.builder()
                .status(FAIL.name())
                .error(error)
                .build();
    }
}
