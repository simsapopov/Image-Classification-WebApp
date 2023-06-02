import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassifyComponent } from './classify.component';

describe('ClassifyComponent', () => {
  let component: ClassifyComponent;
  let fixture: ComponentFixture<ClassifyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClassifyComponent]
    });
    fixture = TestBed.createComponent(ClassifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
