package vn.nlu.huypham.app.service;

import java.time.Instant;
import java.util.UUID;

import vn.nlu.huypham.app.exception.custom.RedisException;

public interface RedisService
{
	boolean checkATBlackList(
		UUID jti);

	void addATBlacklist(
		UUID jti,
		Instant expiredAt) throws RedisException;
}
