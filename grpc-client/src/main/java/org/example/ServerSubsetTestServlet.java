package org.example;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.IOException;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerSubsetTestServlet extends HttpServlet {
    @Inject
    private static GrpcClientHandler grpcClientHandler;

    @Inject
    private static Gson gson;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var data = grpcClientHandler.getServerSubset();
        try (var writer =new BufferedWriter(resp.getWriter())) {
            gson.toJson(data, writer);
            writer.flush();
        }
    }
}
