package vn.nlu.huypham.app.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.nlu.huypham.app.constant.ResourceTypes;
import vn.nlu.huypham.app.constant.ResourceVisibilities;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo
{
	UUID id;
	long size;
	ResourceVisibilities visibility;
	ResourceTypes type;
	long createdAt;
}
