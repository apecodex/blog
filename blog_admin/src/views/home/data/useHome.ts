import type {Ref} from 'vue';
import {StatusCode} from '~/api/enum/statusCode';
import {getWebsiteInfoBack} from '~/api/requests/Website'
import {useWebsiteStore} from '~/store'
import {chinaMapOption} from './area';
import {visitStatisticsOption, dataList, valueList} from './visitStatistics';
import {articleStatisticsOption, articleStatisticsData} from './articleStatistics';
import {articleRankOption, articleRankData} from './articleRank';
import {articleLikeRankOption, articleLikeRankData} from './articleLikeRank';
import {categoryOption, categoriesData} from './category';
import {SystemMemoryOption} from './systemMemory';
import {SystemDiskInfoOption} from './systemDiskInfo';
import {storeToRefs} from "pinia";
import {onMounted, ref} from "vue";
import {TitleOption} from "echarts/types/dist/shared";

const websiteStore = useWebsiteStore()
let {noReadNoticeCount} = storeToRefs(websiteStore)

const tagsName = [] as Array<string>
const isLoading = ref<boolean>(false)
export const useWebsiteBack = () => {
  let websiteInfoBack: Ref<WebsiteInfoBackModel | null> = ref<WebsiteInfoBackModel | null>(null);
  onMounted(async () => {
    isLoading.value = true
    let response = await getWebsiteInfoBack()
    if (response.status && response.code === StatusCode.SUCCESS) {
      websiteInfoBack.value = response.data;
      // 未阅读消息数量
      noReadNoticeCount.value = response.data.noReadNoticeCount
      // @ts-ignore
      // 地图
      chinaMapOption.value.series[0].data = response.data.userVisitAreaStatisticsList.map(area => {
        return {name: area.area, value: area.count}
      });
      // 清空，防止刷新重复添加
      dataList.length = 0
      valueList.length = 0
      // 访问统计
      if (response.data.visitStatisticsList) {
        response.data.visitStatisticsList.forEach((item: VisitStatisticsModel) => {
          dataList.unshift(item.date)
          valueList.unshift(item.viewsCount)
        })
      } else {
        visitStatisticsOption.value.title = {
          text: "用户周访问统计 | 暂无数据",
          x: 'center',
          y: 'center',
        } as TitleOption
      }
      (<any>visitStatisticsOption.value.series).data = valueList;
      (<any>visitStatisticsOption.value.xAxis).data = dataList;
      // 文章统计列表
      articleStatisticsData.length = 0
      if (response.data.articleStatisticsList) {
        response.data.articleStatisticsList.forEach((item: ArticleStatisticsModel) => {
          articleStatisticsData.push([item.date, item.count])
        })
      }
      (<any>articleStatisticsOption.value.series).data.push(...articleStatisticsData);
      // 文章浏览量排行
      articleRankData.length = 0;
      (<any>articleRankOption.value.series).data.length = 0;
      if (response.data.articleRankList) {
        response.data.articleRankList.forEach((item: ArticleRankModel) => {
          articleRankData.push({name: item.articleTitle, value: item.viewsCount})
        })
      } else {
        articleRankOption.value.title = {
          text: "文章浏览量排行 | 暂无数据",
          x: 'center',
          y: 'center',
        } as TitleOption
      }
      (<any>articleRankOption.value.series).data.push(...articleRankData);
      // 文章点赞排行
      articleLikeRankData.length = 0;
      (<any>articleLikeRankOption.value.series).data.length = 0;
      if (response.data.articleLikeRankList) {
        response.data.articleLikeRankList.forEach((item: ArticleLikeRankModel) => {
          articleLikeRankData.push({name: item.articleTitle, value: item.likeCount})
        })
      } else {
        articleLikeRankOption.value.title = {
          text: "文章点赞排行 | 暂无数据",
          x: 'center',
          y: 'center',
        } as TitleOption
      }
      (<any>articleLikeRankOption.value.series).data.push(...articleLikeRankData);
      // 分类列表
      categoriesData.length = 0;
      (<any>categoryOption.value.series).data.length = 0;
      if (response.data.categoryList) {
        response.data.categoryList.forEach((category: CategoryFrontModel) => {
          categoriesData.push({name: category.name, value: category.articleCount})
        })
      } else {
        categoryOption.value.title = {
          text: "分类列表 | 暂无数据",
          x: 'center',
          y: 'center',
        } as TitleOption
      }
      // @ts-ignore
      categoryOption.value.series.data.push(...categoriesData)
      tagsName.length = 0
      // 标签
      if (response.data.tagsName) tagsName.push(...response.data.tagsName)
      else tagsName.push("暂无标签")
      // 系统内存
      SystemMemoryOption.series.max = parseFloat(response.data.systemMemory.total.split("GB")[0])
      SystemMemoryOption.series.data[0].name = `可用：${response.data.systemMemory.available}  利用率：${response.data.systemMemory.ute}`
      SystemMemoryOption.series.data[0].value = parseFloat(response.data.systemMemory.used.split("GB")[0])
      // 系统硬盘
      SystemDiskInfoOption.series.max = parseFloat(response.data.systemDiskInfo[0].total.split("GB")[0])
      SystemDiskInfoOption.series.data[0].name = `可用空间：${response.data.systemDiskInfo[0].free}`
      SystemDiskInfoOption.series.data[0].value = parseFloat(response.data.systemDiskInfo[0].use.split("GB")[0])
    }
    isLoading.value = false
  })

  return {
    websiteInfoBack,
    chinaMapOption,
    visitStatisticsOption,
    articleStatisticsOption,
    articleRankOption,
    articleLikeRankOption,
    categoryOption,
    tagsName,
    SystemMemoryOption,
    SystemDiskInfoOption,
    isLoading
  }
}