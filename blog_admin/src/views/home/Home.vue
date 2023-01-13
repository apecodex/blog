<script setup lang='ts'>
import {useWebsiteBack} from './data/useHome';
import {
  NSpace,
  NGrid,
  NGridItem,
  NCard,
  NStatistic,
  NSkeleton,
  NNumberAnimation,
} from "naive-ui";
import TagCloud from "~/components/TagCloud.vue"

const {
  websiteInfoBack,
  chinaMapOption,
  visitStatisticsOption,
  isLoading,
  articleStatisticsOption,
  articleRankOption,
  articleLikeRankOption,
  categoryOption,
  tagsName,
  SystemMemoryOption,
  SystemDiskInfoOption
} = useWebsiteBack()


</script>
<template>
  <n-space vertical>
    <n-grid cols="2 m:4 l:8 xl:8 2xl:8" style="gap: 8px;" responsive="screen" v-if="websiteInfoBack !== null">
      <n-grid-item>
        <div>
          <n-card hoverable size="small">
            <n-statistic label="文章" tabular-nums>
              <n-number-animation :from="0" :to="websiteInfoBack.articleCount"/>
              <template #suffix>
                篇
              </template>
            </n-statistic>
          </n-card>
        </div>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <n-statistic label="访问量" tabular-nums>
            <n-number-animation :from="0" :to="websiteInfoBack.viewsCount"/>
            <template #suffix>
              次
            </template>
          </n-statistic>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <n-statistic label="独立访问" tabular-nums>
            <n-number-animation :from="0" :to="websiteInfoBack.onlyViewCount"/>
            <template #suffix>
              次
            </template>
          </n-statistic>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <n-statistic label="用户" tabular-nums>
            <n-number-animation :from="0" :to="websiteInfoBack.userCount"/>
            <template #suffix>
              人
            </template>
          </n-statistic>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <n-statistic label="留言" tabular-nums>
            <n-number-animation :from="0" :to="websiteInfoBack.messageCount"/>
            <template #suffix>
              条
            </template>
          </n-statistic>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <n-statistic label="分类" tabular-nums>
            <n-number-animation :from="0" :to="websiteInfoBack.categoryCount"/>
            <template #suffix>
              个
            </template>
          </n-statistic>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <n-statistic label="标签" tabular-nums>
            <n-number-animation :from="0" :to="websiteInfoBack.tagCount"/>
            <template #suffix>
              个
            </template>
          </n-statistic>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <n-statistic label="说说" tabular-nums>
            <n-number-animation :from="0" :to="websiteInfoBack.talkCount"/>
            <template #suffix>
              条
            </template>
          </n-statistic>
        </n-card>
      </n-grid-item>
    </n-grid>
    <n-grid v-else cols="2 m:4 l:8 xl:8 2xl:8" style="gap: 8px;" responsive="screen">
      <n-grid-item v-for="n in 8" :key="n">
        <n-skeleton :height="90" text :sharp="false" size="small"/>
      </n-grid-item>
    </n-grid>
    <n-grid cols="1 m:3" item-responsive style="gap: 8px;" responsive="screen">
      <n-grid-item span="2">
        <n-card hoverable size="small">
          <v-chart class="h-350px" :option="chinaMapOption" autoresize :loading="isLoading"/>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <v-chart class="h-350px" :option="visitStatisticsOption" autoresize :loading="isLoading"/>
        </n-card>
      </n-grid-item>
    </n-grid>
    <n-grid cols="1 m:2" item-responsive style="gap: 8px;" responsive="screen">
      <n-grid-item>
        <n-card hoverable size="small">
          <v-chart class="h-300px" :option="categoryOption" autoresize :loading="isLoading"/>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable v-if="websiteInfoBack !== null && tagsName.length !== 0" size="small">
          <tag-cloud :wordCloud="tagsName"/>
        </n-card>
        <n-card v-else>
          <n-skeleton :height="300" :sharp="false" size="small"/>
        </n-card>
      </n-grid-item>
    </n-grid>
    <n-card hoverable size="small">
      <v-chart class="h-200px" :option="articleStatisticsOption" autoresize :loading="isLoading"/>
    </n-card>
    <n-grid cols="1 m:2" item-responsive style="gap: 8px;" responsive="screen">
      <n-grid-item>
        <n-card hoverable size="small">
          <v-chart class="h-300px" :option="articleRankOption" autoresize :loading="isLoading"/>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <v-chart class="h-300px" :option="articleLikeRankOption" autoresize :loading="isLoading"/>
        </n-card>
      </n-grid-item>
    </n-grid>
    <n-grid cols="1 m:2" item-responsive style="gap: 8px;" responsive="screen" v-if="websiteInfoBack !== null">
      <n-grid-item>
        <n-card hoverable size="small">
          <v-chart class="h-300px" :option="SystemMemoryOption" autoresize :loading="isLoading"/>
        </n-card>
      </n-grid-item>
      <n-grid-item>
        <n-card hoverable size="small">
          <v-chart class="h-300px" :option="SystemDiskInfoOption" autoresize :loading="isLoading"/>
        </n-card>
      </n-grid-item>
    </n-grid>
  </n-space>
</template>

<style scoped>
</style>