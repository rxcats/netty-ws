# Netty

## 🚀 Netty의 핵심 특징

- 비동기 & 논블로킹 I/O
    - Java NIO 기반으로 동작하며, 블로킹 없이 여러 클라이언트를 동시에 처리할 수 있어 효율적입니다.
- 이벤트 기반 구조
    - 네트워크 이벤트(데이터 수신, 연결 등)를 핸들러에 전달하여 처리하는 구조로, 코드가 깔끔하고 유지보수가 쉬워요.
- 높은 성능과 확장성
    - Tomcat이 수천 개의 연결을 처리하는 데 비해, Netty는 수십만 개의 연결도 감당할 수 있습니다.
- 추상화된 API 제공
    - 복잡한 네트워크 로직을 프레임워크가 대신 처리해주므로, 개발자는 비즈니스 로직에 집중할 수 있습니다.

## 🔧 주요 구성 요소

| 구성 요소     | 설명                                                |
|-----------|---------------------------------------------------|
| EventLoop | 이벤트를 감지하고 처리하는 루프. 하나의 스레드가 여러 채널을 관리함.           |
| Channel   | 네트워크 연결을 나타내는 객체. 소켓과 유사한 역할.                     |
| Pipeline  | 이벤트를 핸들러 체인으로 전달하는 구조. Inbound/Outbound 이벤트를 구분함. |
| Handler   | 이벤트를 실제로 처리하는 클래스. 사용자 정의 로직을 구현함.                |

## 📡 Netty의 동작 방식

- 클라이언트가 서버에 연결 요청
- EventLoop가 연결 이벤트를 감지
- Pipeline을 통해 Inbound 이벤트 핸들러로 전달
- 데이터 수신 시 또 다른 Inbound 이벤트 발생
- 응답은 Outbound 이벤트 핸들러를 통해 처리됨

## 테스트를 위한 Simple WebSocket Client

- https://chromewebstore.google.com/detail/simple-websocket-client/gobngblklhkgmjhbpbdlkglbhhlafjnh
