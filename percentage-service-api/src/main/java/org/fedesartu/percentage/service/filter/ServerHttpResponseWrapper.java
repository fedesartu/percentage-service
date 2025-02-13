package org.fedesartu.percentage.service.filter;

import lombok.Getter;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Getter
public class ServerHttpResponseWrapper extends ServerHttpResponseDecorator {

    private final StringBuilder cachedBody = new StringBuilder();
    private final ServerHttpResponse delegate;

    public ServerHttpResponseWrapper(ServerHttpResponse delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        return super.writeWith(Flux.from(body).doOnNext(buffer -> {
            cachedBody.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()));
        }));
    }

    public String getCachedBody() {
        return cachedBody.toString();
    }

    public HttpStatusCode getStatusCode() {
        return delegate.getStatusCode();
    }
}
