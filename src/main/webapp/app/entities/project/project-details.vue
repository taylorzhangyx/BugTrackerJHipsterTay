<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="project">
        <h2 class="jh-entity-heading" data-cy="projectDetailsHeading">
          <span v-text="t$('jhipsterDemoApp.project.detail.title')"></span> {{ project.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('jhipsterDemoApp.project.name')"></span>
          </dt>
          <dd>
            <span>{{ project.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('jhipsterDemoApp.project.description')"></span>
          </dt>
          <dd>
            <span>{{ project.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('jhipsterDemoApp.project.owner')"></span>
          </dt>
          <dd>
            <div v-if="project.owner">
              <router-link :to="{ name: 'TeamView', params: { teamId: project.owner.id } }">{{ project.owner.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="project.id" :to="{ name: 'ProjectEdit', params: { projectId: project.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./project-details.component.ts"></script>
