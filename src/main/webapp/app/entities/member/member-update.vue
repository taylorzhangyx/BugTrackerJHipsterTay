<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="jhipsterDemoApp.member.home.createOrEditLabel"
          data-cy="MemberCreateUpdateHeading"
          v-text="t$('jhipsterDemoApp.member.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="member.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="member.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('jhipsterDemoApp.member.nickName')" for="member-nickName"></label>
            <input
              type="text"
              class="form-control"
              name="nickName"
              id="member-nickName"
              data-cy="nickName"
              :class="{ valid: !v$.nickName.$invalid, invalid: v$.nickName.$invalid }"
              v-model="v$.nickName.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./member-update.component.ts"></script>
