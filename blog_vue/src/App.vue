<script setup lang="ts">
import ConfigProvider from '@/components/layout/ConfigProvider.vue'
import PageLayout from '@/layout/PageLayout.vue'
import Header from '@/components/layout/header/Header.vue'
import AppMain from '@/components/AppMain.vue'
import Footer from '@/components/layout/footer/Footer.vue'
</script>

<template>
  <config-provider>
    <notifications position="top center" classes="vue-notification my-notification"/>
    <page-layout>
      <template #header>
        <Header/>
      </template>
      <template #content>
        <app-main/>
      </template>
      <template #footer>
        <Footer/>
      </template>
    </page-layout>
  </config-provider>
</template>

<style>
.my-notification {
  border-radius: 0 5px 5px 0;
  margin-top: 10px;
  transition: var(--theme-transition);
}
</style>
