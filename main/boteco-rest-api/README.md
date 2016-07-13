# Boteco REST API

This module contains a REST API that allows external communication. Once this bundle is started, you can access
a set of paths:

- `GET cxf/boteco/message/channels`: returns a list of currently available channels
- `POST cxf/boteco/message/{{channel}}`: allows to send a message to a target through the given `channel`

The payload for sending a message is a JSON with the following properties:

- `content`: the content of the message
- `target`: the target of the message

If the response code is **200**, the message will be delivered soon, if the response is **202**, the message will be
delivered as soon as the channel goes online.