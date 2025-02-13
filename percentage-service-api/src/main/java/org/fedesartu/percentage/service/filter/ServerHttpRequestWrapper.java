package org.fedesartu.percentage.service.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ServerHttpRequestWrapper extends ServerHttpRequestDecorator {

    private final StringBuilder cachedBody = new StringBuilder();

    public ServerHttpRequestWrapper(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(this::cache);
    }

    private void cache(DataBuffer buffer) {
        cachedBody.append(UTF_8.decode(buffer.asByteBuffer()));
    }

    public String getCachedBody() {
        return cachedBody.toString();
    }
}


