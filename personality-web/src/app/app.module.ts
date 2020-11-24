import {BrowserModule} from '@angular/platform-browser';
import {Injector, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './components/app/app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexLayoutModule} from "@angular/flex-layout";
import {HomePageComponent} from "./components/home-page/home-page.component";
import {UtilityService} from "./services/utility.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AngularMaterialModule} from "./modules/angular-material.module";
import {PersonalityTestDialogComponent} from "./components/personality-test-dialog/personality-test-dialog.component";
import {QuestionComponent} from "./components/question/question.component";
import {MatSliderModule} from "@angular/material/slider";

@NgModule({
	declarations: [
		AppComponent,
		HomePageComponent,
		PersonalityTestDialogComponent,
		QuestionComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		AngularMaterialModule,
		BrowserAnimationsModule,
		FlexLayoutModule,
		HttpClientModule,
		ReactiveFormsModule,
		FormsModule,
		MatSliderModule
	],
	entryComponents: [PersonalityTestDialogComponent],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule {
	constructor(private injector: Injector) {
		UtilityService.injector = injector;
	}
}
