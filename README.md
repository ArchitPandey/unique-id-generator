# Unique Id Generator for Distributed Systems

This is a basic implementation of twitter's snowflake scheme for generating unique ids in a distributed environment. This implementation can generate around 10k keys / second.

https://blog.twitter.com/engineering/en_us/a/2010/announcing-snowflake

Flickr also has a scheme for generating unique keys in a distributed environment. However, this implementation is only useful for medium scale systems (60 keys / second)

https://code.flickr.net/2010/02/08/ticket-servers-distributed-unique-primary-keys-on-the-cheap/