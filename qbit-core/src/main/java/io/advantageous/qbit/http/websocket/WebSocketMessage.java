package io.advantageous.qbit.http.websocket;

import io.advantageous.qbit.message.Request;
import io.advantageous.qbit.util.MultiMap;

/**
 * Represents a WebSocketMessage
 * Created by rhightower on 10/22/14.
 * @author rhightower
 */
public class WebSocketMessage implements Request<Object>{
    private final String uri;
    private final String message;
    private final WebSocketSender sender;
    private final String remoteAddress;
    private final long messageId;
    private final long timestamp;
    private boolean handled;

    @Override
    public String address() {
        return uri;
    }

    @Override
    public String returnAddress() {
        return remoteAddress;
    }

    @Override
    public MultiMap<String, String> params() {
        return MultiMap.empty();
    }

    @Override
    public MultiMap<String, String> headers() {
        return MultiMap.empty();
    }

    @Override
    public boolean hasParams() {
        return false;
    }

    @Override
    public boolean hasHeaders() {
        return false;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }



    @Override
    public boolean isHandled() {
        return handled;
    }

    @Override
    public void handled() {
        handled = true;
    }

    @Override
    public long id() {
        return messageId;
    }

    @Override
    public Object body() {
        return message;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private static class RequestIdGenerator {
        private long value;
        private long inc() {return value++;}
    }

    private final static ThreadLocal<RequestIdGenerator> idGen = new ThreadLocal<RequestIdGenerator>(){
        @Override
        protected RequestIdGenerator initialValue() {
            return new RequestIdGenerator();
        }
    };



    public WebSocketMessage(final long id, final long timestamp,
            final String uri, final String message, final String remoteAddress, final WebSocketSender sender) {
        this.uri = uri;
        this.message = message;
        this.sender = sender;
        this.remoteAddress = remoteAddress;

        if (id <= 0) {
            this.messageId = idGen.get().inc();
        } else {
            this.messageId = id;
        }


        if (id <= 0) {
            this.timestamp = io.advantageous.qbit.util.Timer.timer().now();

        } else {
            this.timestamp = timestamp;
        }
    }

    public String getUri() {
        return uri;
    }

    public String getMessage() {
        return message;
    }

    public WebSocketSender getSender() {
        return sender;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebSocketMessage that = (WebSocketMessage) o;

        if (messageId != that.messageId) return false;
        if (timestamp != that.timestamp) return false;
        return this.uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (int) (messageId ^ (messageId >>> 32));
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "uri='" + uri + '\'' +
                ", message='" + message + '\'' +
                ", sender=" + sender +
                ", remoteAddress='" + remoteAddress + '\'' +
                ", messageId=" + messageId +
                ", timestamp=" + timestamp +
                ", handled=" + handled +
                '}';
    }
}

