<template>
  <el-table :data="musics" stripe style="width: 100%">
    <el-table-column label="#" width="60" align="center">
      <template #default="{ $index }">
        {{ $index + 1 }}
      </template>
    </el-table-column>
    <el-table-column label="标题">
      <template #default="{ row }">
        <div class="music-item" @click="$emit('play', row)">
          <el-avatar :size="48" :src="row.coverUrl" shape="square" />
          <div class="music-info">
            <div class="music-title">{{ row.title }}</div>
            <div class="music-artist">{{ row.artistName }}</div>
          </div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="专辑" prop="album" width="150" />
    <el-table-column label="时长" prop="duration" width="100" align="center" />
    <el-table-column label="播放" width="100" align="center">
      <template #default="{ row }">
        {{ formatCount(row.playCount) }}
      </template>
    </el-table-column>
    <el-table-column label="操作" width="150" align="center">
      <template #default="{ row }">
        <el-button type="primary" link @click.stop="$emit('play', row)">
          <el-icon><VideoPlay /></el-icon>
        </el-button>
        <el-button type="primary" link @click.stop="$emit('add', row)">
          <el-icon><Plus /></el-icon>
        </el-button>
        <el-button type="primary" link @click.stop="$emit('like', row)">
          <el-icon><Star /></el-icon>
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
defineProps<{
  musics: any[]
}>()

defineEmits<{
  play: [music: any]
  add: [music: any]
  like: [music: any]
}>()

const formatCount = (count: number) => {
  if (count >= 10000) {
    return (count / 10000).toFixed(1) + '万'
  }
  return count
}
</script>

<style scoped>
.music-item {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.music-info {
  flex: 1;
  min-width: 0;
}

.music-title {
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.music-artist {
  font-size: 12px;
  color: #999;
}
</style>
