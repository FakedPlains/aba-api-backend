-- 1.参数列表
-- 1.1 accessKey
local accessKey = ARGV[1]
-- 1.2 dataId
local dataId = ARGV[2]

-- 2.数据 key
-- 2.1 库存 key
local stockKey = 'abapi:interface:invoke:' .. accessKey
-- 2.2 订单 key
local orderKey = 'hmdp:seckill:order:' .. voucherId

-- 3.脚本业务
-- 3.0 判断是否秒杀
if (redis.call('EXISTS', stockKey) == 0) then
    return -1
end
-- 3.1 判断库存是否充足
if (tonumber(redis.call('get', stockKey)) <= 0) then
    -- 3.2 库存不足，返回 1
    return 1
end
-- 3.2 判断用户是否下单
if (redis.call('sismember', orderKey, userId) == 1) then
    -- 3.3 存在，说明重复下单，返回 2
    return 2
end
-- 3.4 扣库存
redis.call('incrby', stockKey, -1)
-- 3.5 下单
redis.call('sadd', orderKey, userId)
-- 3.6 发送消息到队列
redis.call('xadd', 'hmdp.streams.orders', '*', 'userId', userId, 'voucherId', voucherId, 'id', orderId)
return 0
